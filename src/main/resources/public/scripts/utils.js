// mean, median and mode from: https://vhudyma-blog.eu/mean-median-mode-and-range-in-javascript/
const mean = arr => {
  let total = 0;
  for (let i = 0; i < arr.length; i++) {
    total += arr[i];
  }
  return (total / arr.length).toFixed(0);
};

const median = arr => {
  const { length } = arr;

  arr.sort((a, b) => a - b);

  if (length % 2 === 0) {
    return ((arr[length / 2 - 1] + arr[length / 2]) / 2).toFixed(0);
  }

  return (arr[(length - 1) / 2]).toFixed(0);
};

const mode = arr => {
  const mode = {};
  let max = 0, count = 0;

  for(let i = 0; i < arr.length; i++) {
    const item = arr[i];

    if(mode[item]) {
      mode[item]++;
    } else {
      mode[item] = 1;
    }

    if(count < mode[item]) {
      max = item;
      count = mode[item];
    }
  }

  return max.toFixed(0);
};

export {mean, median, mode};